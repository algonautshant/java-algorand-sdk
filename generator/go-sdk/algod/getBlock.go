package algod

import (
	"context"
	"fmt"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
)

// GetBlock /v2/blocks/{round}
// Get the block for the given round.
type GetBlock struct {
	c     *Client
	round uint64
}

// Do performs HTTP request
func (s *GetBlock) Do(ctx context.Context,
	headers ...*common.Header) (response models.BlockResponse, err error) {
	err = s.c.get(ctx, &response,
		fmt.Sprintf("/v2/blocks/%d", s.round), nil, headers)
	return
}
