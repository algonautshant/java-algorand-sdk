package algod

import (
	"context"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// GetStatus /v2/status
// Gets the current node status.
type GetStatus struct {
	c *Client
}

// Do performs HTTP request
func (s *GetStatus) Do(ctx context.Context,
	headers ...*common.Header) (response models.NodeStatusResponse, err error) {
	err = s.c.get(ctx, &response,
		"/v2/status", nil, headers)
	return
}
